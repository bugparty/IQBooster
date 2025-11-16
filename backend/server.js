const express = require('express');
const bodyParser = require('body-parser');
const cors = require('cors');
const sqlite3 = require('sqlite3').verbose();
const bcrypt = require('bcrypt');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = process.env.PORT || 3000;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// Initialize SQLite database
const db = new sqlite3.Database('./braingame.db', (err) => {
  if (err) {
    console.error('Error opening database:', err.message);
  } else {
    console.log('Connected to SQLite database');
    initDatabase();
  }
});

// Create tables if they don't exist
function initDatabase() {
  db.run(`
    CREATE TABLE IF NOT EXISTS users (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      username TEXT UNIQUE NOT NULL,
      password TEXT NOT NULL,
      mailbox TEXT,
      sex TEXT,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP
    )
  `, (err) => {
    if (err) {
      console.error('Error creating users table:', err.message);
    } else {
      console.log('Users table ready');
    }
  });

  db.run(`
    CREATE TABLE IF NOT EXISTS sessions (
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      user_id INTEGER NOT NULL,
      token TEXT UNIQUE NOT NULL,
      created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (user_id) REFERENCES users(id)
    )
  `, (err) => {
    if (err) {
      console.error('Error creating sessions table:', err.message);
    } else {
      console.log('Sessions table ready');
    }
  });
}

// Helper function to create response
function createResponse(statusCode, message, id = null, token = null, userId = null) {
  return {
    status_code: statusCode,
    message: message,
    id: id,
    token: token,
    userId: userId
  };
}

// Register endpoint
app.post('/api/register', async (req, res) => {
  try {
    const { username, password, mailbox, sex } = req.body;

    // Validate input
    if (!username || !password) {
      return res.status(400).json(
        createResponse(400, 'Username and password are required')
      );
    }

    // Check if username already exists
    db.get('SELECT id FROM users WHERE username = ?', [username], async (err, row) => {
      if (err) {
        console.error('Database error:', err.message);
        return res.status(500).json(
          createResponse(500, 'Database error')
        );
      }

      if (row) {
        return res.status(409).json(
          createResponse(409, 'Username already exists')
        );
      }

      // Hash password
      const hashedPassword = await bcrypt.hash(password, 10);

      // Insert new user
      db.run(
        'INSERT INTO users (username, password, mailbox, sex) VALUES (?, ?, ?, ?)',
        [username, hashedPassword, mailbox || '', sex || ''],
        function(err) {
          if (err) {
            console.error('Error inserting user:', err.message);
            return res.status(500).json(
              createResponse(500, 'Error creating user')
            );
          }

          const userId = this.lastID;
          const token = uuidv4();

          // Create session
          db.run(
            'INSERT INTO sessions (user_id, token) VALUES (?, ?)',
            [userId, token],
            function(err) {
              if (err) {
                console.error('Error creating session:', err.message);
                return res.status(500).json(
                  createResponse(500, 'Error creating session')
                );
              }

              console.log(`User registered: ${username} (ID: ${userId})`);
              return res.status(200).json(
                createResponse(200, 'Registration successful', userId, token, userId)
              );
            }
          );
        }
      );
    });
  } catch (error) {
    console.error('Registration error:', error);
    res.status(500).json(
      createResponse(500, 'Internal server error')
    );
  }
});

// Login endpoint
app.post('/api/login', async (req, res) => {
  try {
    const { username, password } = req.body;

    // Validate input
    if (!username || !password) {
      return res.status(400).json(
        createResponse(400, 'Username and password are required')
      );
    }

    // Find user
    db.get(
      'SELECT id, username, password FROM users WHERE username = ?',
      [username],
      async (err, user) => {
        if (err) {
          console.error('Database error:', err.message);
          return res.status(500).json(
            createResponse(500, 'Database error')
          );
        }

        if (!user) {
          return res.status(401).json(
            createResponse(401, 'Invalid username or password')
          );
        }

        // Verify password
        const validPassword = await bcrypt.compare(password, user.password);
        if (!validPassword) {
          return res.status(401).json(
            createResponse(401, 'Invalid username or password')
          );
        }

        // Generate new session token
        const token = uuidv4();

        db.run(
          'INSERT INTO sessions (user_id, token) VALUES (?, ?)',
          [user.id, token],
          function(err) {
            if (err) {
              console.error('Error creating session:', err.message);
              return res.status(500).json(
                createResponse(500, 'Error creating session')
              );
            }

            console.log(`User logged in: ${username} (ID: ${user.id})`);
            return res.status(200).json(
              createResponse(200, 'Login successful', user.id, token, user.id)
            );
          }
        );
      }
    );
  } catch (error) {
    console.error('Login error:', error);
    res.status(500).json(
      createResponse(500, 'Internal server error')
    );
  }
});

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', message: 'IQBooster API is running' });
});

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    message: 'IQBooster Backend API',
    version: '1.0.0',
    endpoints: {
      register: 'POST /api/register',
      login: 'POST /api/login',
      health: 'GET /api/health'
    }
  });
});

// Start server
app.listen(PORT, () => {
  console.log(`========================================`);
  console.log(`IQBooster Backend Server`);
  console.log(`Server is running on port ${PORT}`);
  console.log(`API URL: http://localhost:${PORT}/api`);
  console.log(`========================================`);
});

// Graceful shutdown
process.on('SIGINT', () => {
  db.close((err) => {
    if (err) {
      console.error('Error closing database:', err.message);
    } else {
      console.log('Database connection closed');
    }
    process.exit(0);
  });
});

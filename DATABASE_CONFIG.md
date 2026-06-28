# Database Configuration Guide

## Overview
The Productivity Tracker application supports both **MySQL** and **SQLite** databases. The application uses a flexible configuration system that loads settings from property files and can be overridden by environment variables.

## Configuration Files

### Primary Configuration: `database.properties`
Located at: `src/main/resources/database.properties`

**Default (MySQL) Configuration:**
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/productivity_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=root@123
```

**Alternative (SQLite) Configuration:**
```properties
db.driver=org.sqlite.JDBC
db.url=jdbc:sqlite:productivity.db
db.user= root
db.password= root@123
```

## Switching Database Engines

### To use MySQL (Default):
1. Ensure MySQL server is running on `localhost:3306`
2. Create database: `CREATE DATABASE productivity_db;`
3. Keep default settings in `database.properties`

### To use SQLite:
1. Uncomment SQLite configuration in `database.properties`
2. Comment out MySQL configuration
3. Rebuild the application: `mvn clean install`
4. Redeploy the application

## Environment Variable Override

The application supports environment variable overrides, **but only for valid JDBC URLs**:

```bash
# Valid - will override database.properties
export DB_URL="jdbc:mysql://localhost:3306/productivity_db"
export DB_DRIVER="com.mysql.cj.jdbc.Driver"
export DB_USER="root"
export DB_PASSWORD="root@123"

# Invalid - will be rejected and use database.properties instead
export DB_URL="jdbc:sqlite:productivity.db"  # Must have proper prefix
```

## Supported JDBC URLs

The application accepts the following JDBC URL prefixes:
- `jdbc:mysql://` - MySQL databases
- `jdbc:sqlite:` - SQLite databases  
- `jdbc:postgresql:` - PostgreSQL databases
- `jdbc:h2:` - H2 in-memory databases

## Troubleshooting

### Error: "JDBC Driver not found"
**Cause:** The JDBC driver for the configured database is not in the classpath.

**Solutions:**
1. Ensure `pom.xml` has the correct dependency:
   - For MySQL: `mysql-connector-j`
   - For SQLite: `sqlite-jdbc`
2. Rebuild the project: `mvn clean install`
3. Redeploy to Tomcat

### Error: "Could not initialize class com.productivitytracker.util.DBConnection"
**Cause:** Database driver failed to load or invalid database URL.

**Check:**
1. Verify database server is running (for MySQL/PostgreSQL)
2. Verify database credentials in `database.properties`
3. Check Tomcat logs for the specific error message
4. Ensure no conflicting environment variables are set

### Database not found
**Cause:** Database doesn't exist or wrong database name in URL.

**Solution:**
- For MySQL: `CREATE DATABASE productivity_db;`
- Verify database name matches in `db.url` property

## Deployment Instructions

### Eclipse WTP Deployment:
1. Right-click ProductivityTracker → **Clean Build Folder**
2. Right-click ProductivityTracker → **Publish**
3. Right-click Tomcat Server → **Restart**

### Maven Deployment:
```bash
cd ProductivityTracker
mvn clean install
# Then deploy WAR file to Tomcat
```

## Supported Database Versions

- **MySQL**: 5.7 or higher
- **SQLite**: 3.0 or higher
- **PostgreSQL**: 9.5 or higher (if configured)
- **H2**: 1.0 or higher (if configured)

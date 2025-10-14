# Greeting Form Application

This is a simple web application that greets users by name. It demonstrates the integration of HTML, JavaScript, and PHP.

## Files Included

1. **index.html** - The main HTML page with the form and styling
2. **script.js** - JavaScript for form validation and AJAX handling
3. **greet.php** - PHP backend to process the greeting request

## Requirements

- PHP 7.0 or higher
- A web server (Apache, Nginx, or PHP's built-in server)

## How to Run

### Option 1: Using PHP Built-in Server (Recommended for testing)

```bash
php -S localhost:8000
```

Then open your browser and navigate to: `http://localhost:8000/index.html`

### Option 2: Using Apache or Nginx

1. Copy all three files to your web server's document root
2. Access the page through your web server URL

## Features

- **Client-side Validation**: Checks if the name field is empty before submitting
- **No Page Reload**: Uses AJAX to submit the form without refreshing the page
- **Server-side Validation**: PHP validates the input and sanitizes it to prevent XSS attacks
- **Error Handling**: Displays user-friendly error messages for invalid input
- **Responsive Design**: Works on desktop and mobile devices

## Usage

1. Open the application in your web browser
2. Enter your name in the input field
3. Click the "Submit" button
4. You'll see a personalized greeting message: "Hello, [Your Name]!"

## Testing

The application has been tested for:
- ✅ Empty input validation
- ✅ Whitespace-only input validation
- ✅ Valid name submissions
- ✅ AJAX communication without page reload
- ✅ XSS protection

## Security

The application implements:
- Input validation on both client and server side
- XSS protection using `htmlspecialchars()` in PHP
- Request method validation (POST only)
- JSON-based API responses

## Troubleshooting

**Issue**: "An error occurred. Please try again."
- **Solution**: Make sure the PHP server is running and `greet.php` is accessible

**Issue**: Form doesn't submit
- **Solution**: Check browser console for JavaScript errors and ensure `script.js` is loaded

**Issue**: Blank page
- **Solution**: Verify that PHP is installed and the server is configured correctly

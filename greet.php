<?php
// Set the content type to JSON
header('Content-Type: application/json');

// Check if the request method is POST
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    echo json_encode(['error' => 'Invalid request method. Please use POST.']);
    exit;
}

// Get the name from the POST request
$name = isset($_POST['name']) ? trim($_POST['name']) : '';

// Validate that the name is not empty
if (empty($name)) {
    echo json_encode(['error' => 'Name cannot be empty.']);
    exit;
}

// Sanitize the name to prevent XSS attacks
$name = htmlspecialchars($name, ENT_QUOTES, 'UTF-8');

// Create and return the greeting message
$greeting = "Hello, " . $name . "!";
echo json_encode(['greeting' => $greeting]);
?>

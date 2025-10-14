// Get form and greeting elements
const form = document.getElementById('greetingForm');
const nameInput = document.getElementById('name');
const greetingDiv = document.getElementById('greeting');

// Add event listener for form submission
form.addEventListener('submit', function(event) {
    // Prevent default form submission (page reload)
    event.preventDefault();
    
    // Get the name value and trim whitespace
    const name = nameInput.value.trim();
    
    // Validate that name is not empty
    if (name === '') {
        showMessage('Please enter your name', 'error');
        return;
    }
    
    // Send the name to the server
    sendNameToServer(name);
});

/**
 * Display a message in the greeting div
 * @param {string} message - The message to display
 * @param {string} type - The type of message ('success' or 'error')
 */
function showMessage(message, type) {
    greetingDiv.textContent = message;
    greetingDiv.className = type + ' show';
}

/**
 * Send the name to the PHP backend using fetch API
 * @param {string} name - The name to send to the server
 */
function sendNameToServer(name) {
    // Create form data to send
    const formData = new FormData();
    formData.append('name', name);
    
    // Send POST request to greet.php
    fetch('greet.php', {
        method: 'POST',
        body: formData
    })
    .then(response => {
        // Check if the response is ok
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        // Parse JSON response
        return response.json();
    })
    .then(data => {
        // Check if the response has an error
        if (data.error) {
            showMessage(data.error, 'error');
        } else if (data.greeting) {
            // Display the greeting from the server
            showMessage(data.greeting, 'success');
        } else {
            showMessage('Unexpected response from server', 'error');
        }
    })
    .catch(error => {
        // Handle any errors that occurred during the fetch
        console.error('Error:', error);
        showMessage('An error occurred. Please try again.', 'error');
    });
}

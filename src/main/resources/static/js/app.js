document.addEventListener('DOMContentLoaded', function() {
    const taskInput = document.getElementById('taskInput');
    const submitButton = document.getElementById('submitTask');
    const connectWsButton = document.getElementById('connectWs');
    const responseContainer = document.getElementById('responseContainer');
    
    let socket = null;
    
    // Function to format date
    function formatDate(dateString) {
        const date = new Date(dateString);
        return date.toLocaleString();
    }
    
    // Function to create a response element
    function createResponseElement(response) {
        const responseElement = document.createElement('div');
        responseElement.className = 'agent-response';
        
        // Status class based on status code
        let statusClass = 'status-pending';
        if (response.statusCode === 200) {
            statusClass = 'status-success';
        } else if (response.statusCode >= 400) {
            statusClass = 'status-error';
        }
        
        // Create header with agent name and timestamp
        const header = document.createElement('div');
        header.className = 'agent-header';
        
        const agentName = document.createElement('span');
        agentName.className = 'agent-name';
        agentName.textContent = response.agentName;
        
        const timestamp = document.createElement('span');
        timestamp.className = 'agent-timestamp';
        timestamp.textContent = formatDate(response.timestamp);
        
        header.appendChild(agentName);
        header.appendChild(timestamp);
        
        // Create instructions element
        const instructions = document.createElement('div');
        instructions.className = 'agent-instructions';
        instructions.textContent = `Task: ${response.instructions}`;
        
        // Create steps element
        const steps = document.createElement('div');
        steps.className = 'agent-steps';
        
        if (response.steps && response.steps.length > 0) {
            response.steps.forEach(step => {
                const stepElement = document.createElement('div');
                stepElement.className = 'agent-step';
                stepElement.textContent = step;
                steps.appendChild(stepElement);
            });
        }
        
        // Create output element if there's output
        let output = null;
        if (response.output) {
            output = document.createElement('div');
            output.className = 'agent-output';
            output.textContent = response.output;
        }
        
        // Create status element
        const status = document.createElement('div');
        status.className = `mt-2 ${statusClass}`;
        status.textContent = `Status: ${response.statusCode === 0 ? 'Processing' : response.statusCode}`;
        
        // Append all elements to the response element
        responseElement.appendChild(header);
        responseElement.appendChild(instructions);
        responseElement.appendChild(steps);
        if (output) {
            responseElement.appendChild(output);
        }
        responseElement.appendChild(status);
        
        return responseElement;
    }
    
    // Function to update an existing response element
    function updateResponseElement(existingElement, response) {
        // Update status class
        let statusClass = 'status-pending';
        if (response.statusCode === 200) {
            statusClass = 'status-success';
        } else if (response.statusCode >= 400) {
            statusClass = 'status-error';
        }
        
        // Update timestamp
        const timestamp = existingElement.querySelector('.agent-timestamp');
        timestamp.textContent = formatDate(response.timestamp);
        
        // Update steps
        const stepsContainer = existingElement.querySelector('.agent-steps');
        stepsContainer.innerHTML = '';
        
        if (response.steps && response.steps.length > 0) {
            response.steps.forEach(step => {
                const stepElement = document.createElement('div');
                stepElement.className = 'agent-step';
                stepElement.textContent = step;
                stepsContainer.appendChild(stepElement);
            });
        }
        
        // Update output
        let outputElement = existingElement.querySelector('.agent-output');
        if (response.output) {
            if (!outputElement) {
                outputElement = document.createElement('div');
                outputElement.className = 'agent-output';
                existingElement.insertBefore(outputElement, existingElement.lastChild);
            }
            outputElement.textContent = response.output;
        }
        
        // Update status
        const statusElement = existingElement.querySelector('[class^="mt-2"]');
        statusElement.className = `mt-2 ${statusClass}`;
        statusElement.textContent = `Status: ${response.statusCode === 0 ? 'Processing' : response.statusCode}`;
    }
    
    // Function to handle WebSocket messages
    function handleWebSocketMessage(message) {
        const response = JSON.parse(message.data);
        
        // Check if we already have a response element for this agent
        const existingElement = document.querySelector(`.agent-response .agent-name:contains('${response.agentName}')`);
        
        if (existingElement) {
            // Update existing element
            updateResponseElement(existingElement.parentElement, response);
        } else {
            // Create new element
            const responseElement = createResponseElement(response);
            responseContainer.prepend(responseElement);
        }
    }
    
    // Add contains selector to jQuery
    Element.prototype.contains = function(text) {
        return this.textContent.includes(text);
    };
    
    // Connect to WebSocket
    connectWsButton.addEventListener('click', function() {
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.close();
            connectWsButton.textContent = 'Connect WebSocket';
            return;
        }
        
        // Clear previous responses
        responseContainer.innerHTML = '';
        
        // Create a message indicating connection attempt
        const connectionMessage = document.createElement('div');
        connectionMessage.className = 'text-center text-muted';
        connectionMessage.innerHTML = '<p>Connecting to WebSocket...</p>';
        responseContainer.appendChild(connectionMessage);
        
        // Connect to WebSocket
        socket = new WebSocket(`ws://${window.location.host}/api/ws`);
        
        socket.onopen = function() {
            connectWsButton.textContent = 'Disconnect WebSocket';
            connectionMessage.innerHTML = '<p>WebSocket connected! You can now submit tasks.</p>';
        };
        
        socket.onmessage = handleWebSocketMessage;
        
        socket.onclose = function() {
            connectWsButton.textContent = 'Connect WebSocket';
            const disconnectMessage = document.createElement('div');
            disconnectMessage.className = 'text-center text-muted';
            disconnectMessage.innerHTML = '<p>WebSocket disconnected</p>';
            responseContainer.prepend(disconnectMessage);
        };
        
        socket.onerror = function(error) {
            console.error('WebSocket error:', error);
            connectionMessage.innerHTML = '<p class="text-danger">WebSocket connection error. Check console for details.</p>';
        };
    });
    
    // Submit task via WebSocket
    submitButton.addEventListener('click', function() {
        const task = taskInput.value.trim();
        
        if (!task) {
            alert('Please enter a task');
            return;
        }
        
        if (socket && socket.readyState === WebSocket.OPEN) {
            // Send task via WebSocket
            socket.send(task);
        } else {
            // Send task via REST API
            fetch(`/api/agent/chat?task=${encodeURIComponent(task)}`)
                .then(response => response.json())
                .then(data => {
                    // Clear previous responses
                    responseContainer.innerHTML = '';
                    
                    // Display responses
                    if (data.responses && data.responses.length > 0) {
                        data.responses.forEach(response => {
                            const responseElement = createResponseElement(response);
                            responseContainer.prepend(responseElement);
                        });
                    } else {
                        responseContainer.innerHTML = '<div class="text-center text-muted"><p>No responses received</p></div>';
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    responseContainer.innerHTML = `<div class="text-center text-danger"><p>Error: ${error.message}</p></div>`;
                });
        }
    });
});
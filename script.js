// Floating hearts animation
const heartsContainer = document.getElementById('heartsContainer');
const heartEmojis = ['💜', '💗', '💖', '💕', '💓', '💝', '💘', '🩷'];

function createHeart() {
    const heart = document.createElement('div');
    heart.className = 'heart';
    heart.textContent = heartEmojis[Math.floor(Math.random() * heartEmojis.length)];
    heart.style.left = Math.random() * 100 + '%';
    heart.style.animationDuration = (Math.random() * 3 + 4) + 's';
    heart.style.fontSize = (Math.random() * 20 + 15) + 'px';
    heart.style.animationDelay = Math.random() * 2 + 's';
    
    heartsContainer.appendChild(heart);
    
    setTimeout(() => {
        heart.remove();
    }, 8000);
}

// Create hearts continuously
setInterval(createHeart, 300);

// Initial hearts
for (let i = 0; i < 15; i++) {
    setTimeout(createHeart, i * 200);
}

// No button chase functionality
const noBtn = document.getElementById('noBtn');
const yesBtn = document.getElementById('yesBtn');
const mainCard = document.getElementById('mainCard');
const celebrationScreen = document.getElementById('celebrationScreen');

let noBtnClickAttempts = 0;

noBtn.addEventListener('mouseenter', () => {
    const card = document.querySelector('.card');
    const cardRect = card.getBoundingClientRect();
    const btnRect = noBtn.getBoundingClientRect();
    
    // Calculate random position within viewport but not overlapping with Yes button
    let newX, newY;
    let attempts = 0;
    
    do {
        newX = Math.random() * (window.innerWidth - btnRect.width - 40) + 20;
        newY = Math.random() * (window.innerHeight - btnRect.height - 40) + 20;
        attempts++;
    } while (attempts < 10 && isOverlapping(newX, newY, btnRect.width, btnRect.height));
    
    noBtn.style.position = 'fixed';
    noBtn.style.left = newX + 'px';
    noBtn.style.top = newY + 'px';
    noBtn.style.transform = 'scale(0.9)';
    
    noBtnClickAttempts++;
    
    // Make Yes button bigger and more appealing
    if (noBtnClickAttempts > 2) {
        yesBtn.style.transform = 'scale(1.2)';
        yesBtn.textContent = 'Yes! Please! 💖';
    }
    
    if (noBtnClickAttempts > 5) {
        yesBtn.style.transform = 'scale(1.4)';
        yesBtn.textContent = 'YES YES YES! 💕💕💕';
    }
});

function isOverlapping(x, y, width, height) {
    const yesBtnRect = yesBtn.getBoundingClientRect();
    return !(x + width < yesBtnRect.left ||
             x > yesBtnRect.right ||
             y + height < yesBtnRect.top ||
             y > yesBtnRect.bottom);
}

// Prevent clicking No button
noBtn.addEventListener('click', (e) => {
    e.preventDefault();
    return false;
});

noBtn.addEventListener('touchstart', (e) => {
    e.preventDefault();
    const touch = e.touches[0];
    const newX = Math.random() * (window.innerWidth - 100);
    const newY = Math.random() * (window.innerHeight - 50);
    
    noBtn.style.position = 'fixed';
    noBtn.style.left = newX + 'px';
    noBtn.style.top = newY + 'px';
    
    noBtnClickAttempts++;
    
    if (noBtnClickAttempts > 2) {
        yesBtn.style.transform = 'scale(1.2)';
        yesBtn.textContent = 'Yes! Please! 💖';
    }
});

// Yes button celebration
yesBtn.addEventListener('click', () => {
    // Hide main card
    mainCard.style.display = 'none';
    
    // Show celebration screen
    celebrationScreen.classList.add('show');
    
    // Create massive heart explosion
    createHeartExplosion();
    
    // Create confetti fireworks
    createFireworks();
    
    // Play success animations
    setTimeout(() => {
        createHeartExplosion();
    }, 1000);
    
    setTimeout(() => {
        createFireworks();
    }, 2000);
});

function createHeartExplosion() {
    const colors = ['#9b59b6', '#e91e63', '#ff69b4', '#ffb6d9', '#ffc4d6'];
    const emojis = ['💜', '💗', '💖', '💕', '💓', '💝', '💘', '🩷', '✨', '⭐', '🌟'];
    
    for (let i = 0; i < 50; i++) {
        setTimeout(() => {
            const heart = document.createElement('div');
            heart.className = 'heart';
            heart.textContent = emojis[Math.floor(Math.random() * emojis.length)];
            heart.style.position = 'fixed';
            heart.style.left = '50%';
            heart.style.top = '50%';
            heart.style.fontSize = (Math.random() * 30 + 20) + 'px';
            heart.style.zIndex = '9999';
            heart.style.pointerEvents = 'none';
            
            const angle = (Math.PI * 2 * i) / 50;
            const velocity = Math.random() * 300 + 200;
            const tx = Math.cos(angle) * velocity;
            const ty = Math.sin(angle) * velocity;
            
            heart.animate([
                { transform: 'translate(-50%, -50%) scale(0)', opacity: 1 },
                { transform: `translate(calc(-50% + ${tx}px), calc(-50% + ${ty}px)) scale(1.5)`, opacity: 0 }
            ], {
                duration: 2000,
                easing: 'cubic-bezier(0, .9, .57, 1)'
            });
            
            document.body.appendChild(heart);
            
            setTimeout(() => heart.remove(), 2000);
        }, i * 20);
    }
}

function createFireworks() {
    const fireworksContainer = document.getElementById('fireworks');
    const colors = ['#9b59b6', '#e91e63', '#ff69b4', '#ffb6d9', '#ffc4d6', '#f5e6ff'];
    
    for (let i = 0; i < 5; i++) {
        setTimeout(() => {
            const x = Math.random() * window.innerWidth;
            const y = Math.random() * (window.innerHeight * 0.6);
            
            for (let j = 0; j < 30; j++) {
                const particle = document.createElement('div');
                particle.className = 'firework';
                particle.style.left = x + 'px';
                particle.style.top = y + 'px';
                particle.style.backgroundColor = colors[Math.floor(Math.random() * colors.length)];
                
                const angle = (Math.PI * 2 * j) / 30;
                const velocity = Math.random() * 100 + 50;
                const tx = Math.cos(angle) * velocity;
                const ty = Math.sin(angle) * velocity;
                
                particle.style.setProperty('--x', tx + 'px');
                particle.style.setProperty('--y', ty + 'px');
                
                fireworksContainer.appendChild(particle);
                
                setTimeout(() => particle.remove(), 1000);
            }
        }, i * 500);
    }
}

// Add sparkle effect to images
const heroImage = document.getElementById('heroImage');

heroImage.addEventListener('mouseenter', () => {
    createSparkles(heroImage);
});

function createSparkles(element) {
    const rect = element.getBoundingClientRect();
    
    for (let i = 0; i < 10; i++) {
        setTimeout(() => {
            const sparkle = document.createElement('div');
            sparkle.textContent = '✨';
            sparkle.style.position = 'fixed';
            sparkle.style.left = rect.left + Math.random() * rect.width + 'px';
            sparkle.style.top = rect.top + Math.random() * rect.height + 'px';
            sparkle.style.fontSize = '20px';
            sparkle.style.pointerEvents = 'none';
            sparkle.style.zIndex = '100';
            
            sparkle.animate([
                { transform: 'translateY(0) scale(0)', opacity: 1 },
                { transform: 'translateY(-50px) scale(1.5)', opacity: 0 }
            ], {
                duration: 1000,
                easing: 'ease-out'
            });
            
            document.body.appendChild(sparkle);
            
            setTimeout(() => sparkle.remove(), 1000);
        }, i * 100);
    }
}

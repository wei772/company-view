function removeLoginErrorHighlights() {
    document.getElementById('cv-username-field').classList.remove('has-error');
    document.getElementById('cv-password-field').classList.remove('has-error');
}

function addLoginErrorHighlights() {
    document.getElementById('cv-username-field').classList.add('has-error');
    document.getElementById('cv-password-field').classList.add('has-error');
}
function validatePassword() {
	password.value != confirm_password.value ? confirm_password.setCustomValidity("A két jelszó nem egyezik.") : confirm_password.setCustomValidity("")
}

function validateEmail() {
	email.value != confirm_email.value ? confirm_email.setCustomValidity("A két e-mail nem egyezik.") : confirm_email.setCustomValidity("")
}

var password = document.getElementById("password"),
confirm_password = document.getElementById("confirm_password");
password.onchange = validatePassword, confirm_password.onkeyup = validatePassword;
var email = document.getElementById("email"),
confirm_email = document.getElementById("confirm_email");
email.onchange = validateEmail, confirm_email.onkeyup = validateEmail;

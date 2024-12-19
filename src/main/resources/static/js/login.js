function togglePassword() {
  inputPassword = document.querySelector("#password-login");
  eye = document.querySelector("#eye");
  eyeSlash = document.querySelector("#eye-slash");
  if (inputPassword.type === "password") {
    inputPassword.type = "text";
    eyeSlash.classList.add("hidden");
    eye.classList.remove("hidden");
  } else {
    inputPassword.type = "password";
    eye.classList.add("hidden");
    eyeSlash.classList.remove("hidden");
  }
}

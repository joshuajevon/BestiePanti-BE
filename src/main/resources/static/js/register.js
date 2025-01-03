function togglePassword() {
  inputPassword = document.querySelector("#kata-sandi-register");
  eye = document.querySelector("#eye-1");
  eyeSlash = document.querySelector("#eye-slash-1");
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

function toggleConfirmPassword() {
  inputConfirmPassword = document.querySelector(
    "#konfirmasi-kata-sandi-register",
  );
  eye = document.querySelector("#eye-2");
  eyeSlash = document.querySelector("#eye-slash-2");
  if (inputConfirmPassword.type === "password") {
    inputConfirmPassword.type = "text";
    eyeSlash.classList.add("hidden");
    eye.classList.remove("hidden");
  } else {
    inputConfirmPassword.type = "password";
    eye.classList.add("hidden");
    eyeSlash.classList.remove("hidden");
  }
}

function SubmitForm(e) {
  e.preventDefault();

  const name = $("name").value;
  const email = $("email").value;
  const password = $("password").value;
  const confirmPassword = $("confirm-password").value;
  const phone = $("phone").value;
  const gender = $("gender").value;
  const dob = $("dob").value;
  const address = $("address").value;

  const data = {
    name,
    email,
    password,
    phone,
    gender,
    dob,
    address,
  };

  if (password === confirmPassword) {
    const jsonData = JSON.stringify(data);
    fetch("/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: jsonData,
    })
      .then((response) => {
        if (response.status == 201) {
          console.log("Success:", data);
          window.location.href = "/";
        } else {
          return response.json().then((errorData) => {
            console.error("Registration failed:", errorData);
            alert("Registration failed. Please try again.");
          });
        }
      })
      .catch((error) => {
        console.error("Error:", error);
        alert("An error occurred. Please try again.");
      });
  } else {
    alert("Passwords do not match");
  }
}

function togglePassword() {
  inputPassword = $("#password");
  eye = $("#eye-1");
  eyeSlash = $("#eye-slash-1");
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
  inputConfirmPassword = $("#confirm-password");
  eye = $("#eye-2");
  eyeSlash = $("#eye-slash-2");
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

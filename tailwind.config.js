import defaultTheme from "tailwindcss/defaultTheme";
import forms from "@tailwindcss/forms";

/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/main/resources/templates/**/*.{html,js}",
    "./src/main/resources/static/**/*.{html,js}",
  ],

  theme: {
    extend: {
      fontFamily: {
        sans: ["Roboto", ...defaultTheme.fontFamily.sans],
      },
      colors: {
        primary: {
          50: "#F6F8FE",
          100: "#E8EFFF",
          300: "#516FFF",
          500: "#2547FA",
          900: "#110843",
        },
        secondary: {
          100: "#E5E9F2",
          500: "#34364A",
        },
      },
    },
  },

  plugins: [forms],
};

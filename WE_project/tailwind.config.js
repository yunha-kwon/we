/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
    "./node_modules/flowbite/**/*.js",
  ],
  theme: {
    extend: {
      fontFamily: {
        nanum: ["Nanummyeongjo", "serif"], // Nanummyeongjo 폰트 추가
      },
    },
  },
  plugins: [],
};

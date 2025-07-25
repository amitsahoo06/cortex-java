import tailwindcss from "@tailwindcss/vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import {defineConfig} from "vite";
import svgr from "vite-plugin-svgr";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss(), svgr()],
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src"),
    },
  },
  preview: {
    port: 12001,
    strictPort: true,
  },
  server: {
    port: 12001,
    strictPort: true,
    host: true,
    cors: true,
    proxy: {
      '/api': {
        target: 'http://localhost:12000',
        changeOrigin: true,
        secure: false,
        ws: true
      }
    }
  },
});

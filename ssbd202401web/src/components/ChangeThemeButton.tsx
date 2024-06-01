import { Brightness4, Brightness7 } from "@mui/icons-material";
import { Box, IconButton } from "@mui/material";
import { useAccount } from "../hooks/useAccount";

export default function ChangeThemeButton() {
  const { isAuthenticated, theme, setMyTheme, setTheme } = useAccount();

  const handleDarkModeToggle = () => {
    if (theme === "Dark") {
      if (isAuthenticated) {
        setMyTheme("Light");
      } else {
        localStorage.setItem("theme", "Light");
      }
      setTheme("Light");
    } else {
      if (isAuthenticated) {
        setMyTheme("Dark");
      } else {
        localStorage.setItem("theme", "Dark");
      }
      setTheme("Dark");
    }
  };
  return (
    <Box sx={{ flexGrow: 0, display: "flex", alignItems: "center" }}>
      <IconButton onClick={handleDarkModeToggle}>
        {theme === "Dark" ? (
          <Brightness4 />
        ) : (
          <Brightness7 sx={{ color: "white" }} />
        )}
      </IconButton>
    </Box>
  );
}

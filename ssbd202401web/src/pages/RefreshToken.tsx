import { useEffect, useState } from "react";
import { useAccount } from "../hooks/useAccount";
import ModalComponent from "../components/ModalComponent";
import { useTranslation } from "react-i18next";
import {
  Box,
  Button,
  CssBaseline,
  ThemeProvider,
  Typography,
} from "@mui/material";
import { PublicDarkTheme, PublicTheme } from "../themes/themes";

export default function RefreshToken() {
  const [open, setOpen] = useState(false);
  const { t } = useTranslation();
  const { parsedToken, theme, refreshToken } = useAccount();
  const isDarkMode = theme === "Dark";

  useEffect(() => {
    if (parsedToken) {
      if (parsedToken?.exp - Math.round(new Date().getTime() / 1000) <= 0) {
        setOpen(false);
      } else {
        const id = setTimeout(
          () => {
            setOpen(true);
          },
          (parsedToken?.exp - Math.round(new Date().getTime() / 1000) - 60) *
            1000,
        );

        return () => clearTimeout(id);
      }
    }
  }, [parsedToken]);

  return (
    <ThemeProvider theme={isDarkMode ? PublicDarkTheme : PublicTheme}>
      <CssBaseline></CssBaseline>
      <ModalComponent onClose={() => {}} open={open}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            width: "100%",
            height: "100%",
          }}
        >
          <Typography variant="h4" textAlign={"center"} margin={4}>
            {t("refreshModalTitle")}
          </Typography>
          <Typography variant="body1" textAlign={"center"} margin={4}>
            {t("refreshModalBody")}
          </Typography>
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              alignItems: "center",
              width: "60%",
            }}
          >
            <Button
              variant="contained"
              onClick={() => {
                refreshToken();
                setOpen(false);
              }}
            >
              Refresh
            </Button>
            <Button variant="contained" onClick={() => setOpen(false)}>
              Cancel
            </Button>
          </Box>
        </Box>
      </ModalComponent>
    </ThemeProvider>
  );
}

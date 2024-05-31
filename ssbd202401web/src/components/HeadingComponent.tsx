import { Typography } from "@mui/material";
import { Link } from "react-router-dom";
import { useWindowWidth } from "../hooks/useWindowWidth";
import HomeIcon from "@mui/icons-material/Home";

export default function HeadingComponent() {
  const width = useWindowWidth();
  return (
    <Typography
      variant="h6"
      sx={{
        mr: 2,
        display: "flex",
        fontWeight: "600",
        fontSize: "24px",
      }}
    >
      <Link
        style={{
          textDecoration: "none",
          color: "inherit",
        }}
        to={"/"}
      >
        {width < 1020 ? (
          <HomeIcon
            sx={{
              display: "flex",
            }}
          ></HomeIcon>
        ) : (
          "EventSymphony"
        )}
      </Link>
    </Typography>
  );
}

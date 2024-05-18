import { LinkPropType } from "../types/Components";
import { MenuItem, Typography } from "@mui/material";

export default function LinkComponent(props: LinkPropType) {
  return (
    <MenuItem
      onClick={() => {
        props.handleClose();
        props.onClick();
      }}
    >
      <Typography
        textAlign={"center"}
        sx={{
          color: "inherit",
          fontSize: "18px",
          fontWeight: 500,
        }}
      >
        {props.name}
      </Typography>
    </MenuItem>
  );
}

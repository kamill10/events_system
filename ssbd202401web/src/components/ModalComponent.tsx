import { Box, Modal } from "@mui/material";
import { ReactElement } from "react";

const style = {
  position: "absolute" as "absolute",
  top: "50%",
  left: "50%",
  transform: "translate(-50%, -50%)",
  width: 400,
  overflow: "auto",
  bgcolor: "background.paper",
  boxShadow: 24,
  p: 4,
  display: "flex",
  flexDirection: "column",
  alignItems: "center",
};

export default function ModalComponent({
  open,
  onClose,
  children,
  width
}: {
  open: boolean;
  onClose: () => void;
  children?: ReactElement;
  width?: number;
}) {
  return (
    <Modal open={open} onClose={onClose}>
      <Box sx={{
        ...style,
        width: width ?? style.width
      }}>{children}</Box>
    </Modal>
  );
}

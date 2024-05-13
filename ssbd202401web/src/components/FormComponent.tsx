import { Box } from "@mui/material";
import { FormPropsType } from "../types/FormPropsType";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { FieldValue, FieldValues } from "react-hook-form";

export default function FormComponent<T extends FieldValues>(
  props: FormPropsType<T>,
) {
  const { handleSubmit, onSubmit, onError, children, align } = props;
  return (
    <Box
      component={"form"}
      onSubmit={handleSubmit(onSubmit, onError)}
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: align ? align : "center",
        marginY: 4,
        marginX: 8,
      }}
    >
      {children}
    </Box>
  );
}

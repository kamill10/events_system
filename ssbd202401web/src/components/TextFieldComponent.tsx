import { TextField } from "@mui/material";
import { Controller, FieldValues, Path } from "react-hook-form";

import { TextFieldProps } from "../types/TextFieldProps";

export default function TextFieldComponent<T extends FieldValues>(props: TextFieldProps<T>) {
  const {
    control,
    name,
    trigger,
    errors
  } = props;
  return (
    <Controller
      name={name}
      control={control}
      render={({ field }) => {
        return (
          <TextField
            sx={{ marginTop: "1rem" }}
            value={field.value}
            onChange={(e) => {
              field.onChange(e);
              setTimeout(
                () => trigger(e.target.name as Path<T>),
                500,
              );
            }}
            id={field.name}
            label="First name"
            name={field.name}
            autoComplete=""
            fullWidth
            error={errors.firstName ? true : false}
          ></TextField>
        );
      }}
    ></Controller>
  )
}
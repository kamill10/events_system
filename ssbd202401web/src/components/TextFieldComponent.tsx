import { TextField, Typography } from "@mui/material";
import { Controller, FieldValues, Path } from "react-hook-form";

import { TextFieldProps } from "../types/TextFieldProps";

export default function TextFieldComponent<T extends FieldValues>(
  props: TextFieldProps<T>,
) {
  const { control, name, trigger, errors, label, type } = props;
  return (
    <>
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
                setTimeout(() => trigger(e.target.name as Path<T>), 500);
              }}
              id={field.name}
              label={label}
              name={field.name}
              autoComplete=""
              fullWidth
              type={type}
              error={errors[name] ? true : false}
            ></TextField>
          );
        }}
      ></Controller>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors[name]?.message as string}
      </Typography>
    </>
  );
}

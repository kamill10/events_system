import {
  IconButton,
  InputAdornment,
  TextField,
  Typography,
} from "@mui/material";
import { Controller, FieldValues, Path } from "react-hook-form";
import { TextFieldProps } from "../types/Components";
import { useState } from "react";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";

export default function TextFieldComponent<T extends FieldValues>(
  props: TextFieldProps<T>,
) {
  const { control, name, trigger, errors, label, type, disabled, focused } =
    props;
  const [textVisible, setTextVisible] = useState(false);

  if (type === "password")
    return (
      <>
        <Controller
          name={name}
          control={control}
          render={({ field }) => {
            return (
              <TextField
                sx={{
                  marginTop: "1rem",
                }}
                value={field.value}
                onChange={(e) => {
                  field.onChange(e);
                  setTimeout(() => trigger(e.target.name as Path<T>), 500);
                  setTextVisible(false);
                }}
                id={field.name}
                label={label}
                name={field.name}
                autoComplete=""
                fullWidth
                focused={focused}
                type={textVisible ? "text" : "password"}
                error={errors[name] ? true : false}
                disabled={disabled}
                InputProps={{
                  endAdornment: (
                    <InputAdornment position="end">
                      <IconButton
                        aria-label="toggle password visibility"
                        onClick={() => setTextVisible(!textVisible)}
                      >
                        {textVisible ? <VisibilityOff /> : <Visibility />}
                      </IconButton>
                    </InputAdornment>
                  ),
                }}
              ></TextField>
            );
          }}
        ></Controller>
        <Typography
          color={"red"}
          fontSize={14}
          width={"inherit"}
          margin={"none"}
        >
          {errors[name]?.message as string}
        </Typography>
      </>
    );

  return (
    <>
      <Controller
        name={name}
        control={control}
        render={({ field }) => {
          return (
            <TextField
              sx={{
                marginTop: "1rem",
              }}
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
              disabled={disabled}
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

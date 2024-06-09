import { Controller, FieldValues } from "react-hook-form";
import { DatePickerProps } from "../types/Components";
import { DatePicker } from "@mui/x-date-pickers";
import { Typography } from "@mui/material";

export default function DatePickerComponent<T extends FieldValues>(
  props: DatePickerProps<T>,
) {
  const { control, name, label, errors } = props;
  return (
    <>
      <Controller
        name={name}
        control={control}
        render={({ field }) => {
          return (
            <DatePicker
              label={label}
              sx={{
                marginTop: "1rem",
              }}
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
              }}
              name={field.name}
              format="DD-MM-YYYY"
            ></DatePicker>
          );
        }}
      ></Controller>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors[name]?.message as string}
      </Typography>
    </>
  );
}

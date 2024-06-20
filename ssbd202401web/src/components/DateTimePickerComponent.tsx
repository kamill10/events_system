import { Typography } from "@mui/material";
import { DateTimePicker } from "@mui/x-date-pickers";
import { Controller, FieldValues, Path } from "react-hook-form";
import { DatePickerProps } from "../types/Components";

export default function DateTimePickerComponent<T extends FieldValues>(
  props: DatePickerProps<T>,
) {
  const {
    control,
    name,
    label,
    errors,
    trigger,
    whatToValidate,
    minDate,
    maxDate,
  } = props;
  return (
    <>
      <Controller
        name={name}
        control={control}
        render={({ field }) => {
          return (
            <DateTimePicker
              label={label}
              sx={{
                marginTop: "1rem",
              }}
              slotProps={{
                textField: {
                  error: errors[name] ? true : false,
                },
              }}
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
                trigger(name);
                whatToValidate?.forEach((element) =>
                  trigger(element as Path<T>),
                );
              }}
              name={field.name}
              format="DD-MM-YYYY H:mm:ss"
              minDateTime={minDate}
              maxDateTime={maxDate}
            ></DateTimePicker>
          );
        }}
      ></Controller>
      <Typography color={"red"} fontSize={14} width={"inherit"} margin={"none"}>
        {errors[name]?.message as string}
      </Typography>
    </>
  );
}

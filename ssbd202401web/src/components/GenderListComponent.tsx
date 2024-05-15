import { TextField, MenuItem, Typography } from "@mui/material";
import { Controller, FieldValues } from "react-hook-form";
import { GenderEnum } from "../types/enums/Gender.enum";
import { GenderListProps } from "../types/Components";

export default function GenderListComponent<T extends FieldValues>(props: GenderListProps<T>) {
  const { control, errors, disabled, name } = props;
  return (
    <>
    <Controller
        name={name}
        control={control}
        render={({ field }) => {
          return (
            <TextField
              select
              label="Gender"
              value={field.value}
              onChange={(e) => {
                field.onChange(e);
              }}
              id={field.name}
              sx={{ marginTop: "1rem" }}
              name={field.name}
              autoComplete=""
              disabled={disabled}
            >
              {Object.keys(GenderEnum).map((key, value) => {
                return (
                  <MenuItem key={key} value={value}>
                    {GenderEnum[key as keyof typeof GenderEnum].info}
                  </MenuItem>
                );
              })}
            </TextField>
          );
        }}
      ></Controller>
      <Typography
        color={"red"}
        fontSize={14}
        width={"inherit"}
      >
        {errors.gender?.message as string}
      </Typography></>
  )
}
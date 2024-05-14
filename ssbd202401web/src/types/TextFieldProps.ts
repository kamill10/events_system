import { HTMLInputTypeAttribute } from "react";
import {
  Control,
  FieldErrors,
  FieldValues,
  Path,
  UseFormTrigger,
} from "react-hook-form";

export interface TextFieldProps<T extends FieldValues> {
  control: Control<T>;
  type: HTMLInputTypeAttribute;
  errors: FieldErrors<T>;
  trigger: UseFormTrigger<T>;
  name: Path<T>;
  label: string;
}

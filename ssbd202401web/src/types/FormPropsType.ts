import {
  FieldValues,
  SubmitErrorHandler,
  SubmitHandler,
  UseFormHandleSubmit,
} from "react-hook-form";
import { ReactNode } from "react";

export interface FormPropsType<T extends FieldValues> {
  handleSubmit: UseFormHandleSubmit<T, undefined>;
  onSubmit: SubmitHandler<T>;
  onError: SubmitErrorHandler<T>;
  children: ReactNode;
  align?: "center" | "start" | "end" | undefined;
}

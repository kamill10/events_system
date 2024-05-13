import { FieldValue, FieldValues, SubmitErrorHandler, SubmitHandler, UseFormHandleSubmit } from "react-hook-form";
import { ForgotPasswordType } from "./ForgotPassword";
import { ReactNode } from "react";

export interface FormPropsType<T extends FieldValues> {
  handleSubmit: UseFormHandleSubmit<T, undefined>;
  onSubmit: SubmitHandler<T>;
  onError: SubmitErrorHandler<T>;
  children: ReactNode
}
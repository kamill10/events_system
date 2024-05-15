import * as yup from "yup";
import { UpdatePersonalDataType, ChangeMyPasswordType, ChangeMyEmailType, ManageAccountType, ChangeEmailType } from "../types/Account";
import { SignInCredentialsType, LoginCredentialsType, ForgotPasswordType, ResetPasswordType } from "../types/Authentication";

export const signInValidationSchema = yup.object<SignInCredentialsType>().shape({
  username: yup
    .string()
    .min(2)
    .max(32)
    .matches(/[\w+]/)
    .notOneOf(["anonymous"])
    .required(),
  password: yup.string().min(8).max(72).required(),
  confirmPassword: yup
    .string()
    .oneOf([yup.ref("password")], "Passwords don't match")
    .required(),
  email: yup.string().email().required(),
  gender: yup.number().required(),
  firstName: yup.string().min(2).max(32).matches(/[\w+]/).required(),
  lastName: yup.string().min(2).max(64).matches(/[\w+]/).required(),
  language: yup.string().required(),
});

export const LogInSchema = yup.object<LoginCredentialsType>().shape({
  username: yup.string().min(2).max(32).matches(/[\w+]/).required(),
  password: yup.string().min(8).max(72).required(),
});

export const changePersonalDataSchema = yup.object<UpdatePersonalDataType>().shape({
  gender: yup.number().required(),
  firstName: yup.string().min(2).max(32).matches(/[\w+]/).required(),
  lastName: yup.string().min(2).max(64).matches(/[\w+]/).required(),
});

export const ForgotPasswordSchema = yup.object<ForgotPasswordType>().shape({
  email: yup.string().email().required(),
});

export const ResetPasswordSchema = yup.object<ResetPasswordType>().shape({
  newPassword: yup.string().min(8).max(72).required(),
  confirmNewPassword: yup
    .string()
    .oneOf([yup.ref("newPassword")], "Passwords don't match")
    .required(),
});

export const ChangeMyPasswordSchema = yup.object<ChangeMyPasswordType>().shape({
  oldPassword: yup.string().min(8).max(64).required(),
  newPassword: yup
    .string()
    .min(8)
    .max(64)
    .required()
    .notOneOf(
      [yup.ref("oldPassword")],
      "New password must be different from the old one",
    ),
  confirmNewPassword: yup
    .string()
    .oneOf([yup.ref("newPassword")], "Passwords don't match")
    .required(),
});

export const ChangeMyEmailSchema = yup.object<ChangeMyEmailType>().shape({
  password: yup.string().min(8).max(72).required(),
  email: yup.string().email().required(),
});

export const ManageAccountValidationSchema = yup.object<ManageAccountType>().shape({
  email: yup.string().email().required(),
  password: yup.string().min(8).max(72).required(),
  firstName: yup.string().min(2).max(32).matches(/[\w+]/).required(),
  lastName: yup.string().min(2).max(64).matches(/[\w+]/).required(),
  gender: yup.number().required()
});

export const ChangeEmailSchema = yup.object<ChangeEmailType>().shape({
  email: yup.string().min(8).max(72).required(),
});
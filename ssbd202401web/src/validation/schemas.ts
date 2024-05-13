import * as yup from "yup";
import { AccountLoginType, AccountSingInType } from "../types/Account";
import { PersonalDataType } from "../types/PersonalData";
import { ForgotPasswordType } from "../types/ForgotPassword";
import { ResetPasswordType } from "../types/ResetPasswordType";
import { ChangeMyPasswordType } from "../types/ChangeMyPasswordType.ts";

export const signInValidationSchema = yup.object<AccountSingInType>().shape({
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

export const LogInSchema = yup.object<AccountLoginType>().shape({
  username: yup.string().min(2).max(32).matches(/[\w+]/).required(),
  password: yup.string().min(8).max(72).required(),
});

export const changePersonalDataSchema = yup.object<PersonalDataType>().shape({
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
  newPassword: yup.string().min(8).max(64).required(),
  confirmNewPassword: yup
    .string()
    .oneOf([yup.ref("newPassword")], "Passwords don't match")
    .required(),
});

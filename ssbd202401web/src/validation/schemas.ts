import * as yup from "yup";
import {
  UpdatePersonalDataType,
  ChangeMyPasswordType,
  ChangeMyEmailType,
  ManageAccountType,
  ChangeEmailType,
} from "../types/Account";
import {
  SignInCredentialsType,
  LoginCredentialsType,
  ForgotPasswordType,
  ResetPasswordType,
} from "../types/Authentication";
import { SortingRequestParams } from "../types/SortingRequestParams";
import i18next from "i18next";

export let signInValidationSchema: yup.ObjectSchema<SignInCredentialsType>;
export let LogInSchema: yup.ObjectSchema<LoginCredentialsType>;
export let changePersonalDataSchema: yup.ObjectSchema<UpdatePersonalDataType>;
export let ForgotPasswordSchema: yup.ObjectSchema<ForgotPasswordType>;
export let ResetPasswordSchema: yup.ObjectSchema<ResetPasswordType>;
export let ChangeMyPasswordSchema: yup.ObjectSchema<ChangeMyPasswordType>;
export let ChangeMyEmailSchema: yup.ObjectSchema<ChangeMyEmailType>;
export let ManageAccountValidationSchema: yup.ObjectSchema<ManageAccountType>;
export let ChangeEmailSchema: yup.ObjectSchema<ChangeEmailType>;
export let SortingRequestParamsSchema: yup.ObjectSchema<SortingRequestParams>;

export function initValidation() {
  signInValidationSchema = yup.object<SignInCredentialsType>().shape({
    username: yup
      .string()
      .min(3, i18next.t("usernameTooShort"))
      .max(32, i18next.t("usernameTooLong"))
      .matches(/[\w+]/, i18next.t("usernameWrongFormat"))
      .notOneOf(["anonymous"], i18next.t("usernameWrongFormat"))
      .required(i18next.t("usernameRequired")),
    password: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .matches(
        /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[\W_])[A-Za-z\d\W_]{8,72}$/,
        i18next.t("passwordWrongFormat"),
      )
      .required(i18next.t("passwordRequired")),
    confirmPassword: yup
      .string()
      .oneOf([yup.ref("password")], i18next.t("passwordsDontMatch"))
      .required(i18next.t("confirmPasswordRequired")),
    email: yup
      .string()
      .email(i18next.t("emailWrongFormat"))
      .required(i18next.t("emailRequired")),
    gender: yup.number().required(),
    firstName: yup
      .string()
      .min(2, i18next.t("firstNameTooShort"))
      .max(32, i18next.t("firstNameTooLong"))
      .matches(/[\w+]/, i18next.t("firstNameWrongFormat"))
      .required(i18next.t("firstNameRequired")),
    lastName: yup
      .string()
      .min(2, i18next.t("lastNameTooShort"))
      .max(64, i18next.t("lastNameTooLong"))
      .matches(/[\w+]/, i18next.t("lastNameWrongFormat"))
      .required(i18next.t("lastNameRequired")),
    language: yup.string().required(),
  });

  LogInSchema = yup.object<LoginCredentialsType>().shape({
    username: yup
      .string()
      .min(3, i18next.t("usernameTooShort"))
      .max(32, i18next.t("usernameTooLong"))
      .matches(/[\w+]/, i18next.t("usernameWrongFormat"))
      .required(i18next.t("usernameRequired")),
    password: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired")),
  });

  changePersonalDataSchema = yup.object<UpdatePersonalDataType>().shape({
    gender: yup.number().required(),
    firstName: yup
      .string()
      .min(2, i18next.t("firstNameTooShort"))
      .max(32, i18next.t("firstNameTooLong"))
      .matches(/[\w+]/, i18next.t("firstNameWrongFormat"))
      .required(i18next.t("firstNameRequired")),
    lastName: yup
      .string()
      .min(2, i18next.t("lastNameTooShort"))
      .max(64, i18next.t("lastNameTooLong"))
      .matches(/[\w+]/, i18next.t("lastNameWrongFormat"))
      .required(i18next.t("lastNameRequired")),
    timeZone: yup.string().required(),
    theme: yup.string().required(),
  });

  ForgotPasswordSchema = yup.object<ForgotPasswordType>().shape({
    email: yup
      .string()
      .email(i18next.t("emailWrongFormat"))
      .required(i18next.t("emailRequired")),
  });

  ResetPasswordSchema = yup.object<ResetPasswordType>().shape({
    newPassword: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired")),
    confirmNewPassword: yup
      .string()
      .oneOf([yup.ref("newPassword")], i18next.t("passwordsDontMatch"))
      .required(i18next.t("confirmPasswordRequired")),
    token: yup.string(),
  });

  ChangeMyPasswordSchema = yup.object<ChangeMyPasswordType>().shape({
    oldPassword: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired")),
    newPassword: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired"))
      .notOneOf(
        [yup.ref("oldPassword")],
        i18next.t("newPasswordDifferentThanOld"),
      ),
    confirmNewPassword: yup
      .string()
      .oneOf([yup.ref("newPassword")], i18next.t("passwordsDontMatch"))
      .required(i18next.t("confirmPasswordRequired")),
  });

  ChangeMyEmailSchema = yup.object<ChangeMyEmailType>().shape({
    password: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired")),
    newEmail: yup
      .string()
      .email(i18next.t("emailWrongFormat"))
      .required(i18next.t("emailRequired")),
  });

  ManageAccountValidationSchema = yup.object<ManageAccountType>().shape({
    email: yup
      .string()
      .email(i18next.t("emailWrongFormat"))
      .required(i18next.t("emailRequired")),
    password: yup
      .string()
      .min(8, i18next.t("passwordTooShort"))
      .max(72, i18next.t("passwordTooLong"))
      .required(i18next.t("passwordRequired")),
    firstName: yup
      .string()
      .min(2, i18next.t("firstNameTooShort"))
      .max(32, i18next.t("firstNameTooLong"))
      .matches(/[\w+]/, i18next.t("firstNameWrongFormat"))
      .required(i18next.t("firstNameRequired")),
    lastName: yup
      .string()
      .min(2, i18next.t("lastNameTooShort"))
      .max(64, i18next.t("lastNameTooLong"))
      .matches(/[\w+]/, i18next.t("lastNameWrongFormat"))
      .required(i18next.t("lastNameRequired")),
    gender: yup.number().required(),
  });

  ChangeEmailSchema = yup.object<ChangeEmailType>().shape({
    email: yup
      .string()
      .email(i18next.t("emailWrongFormat"))
      .required(i18next.t("emailRequired")),
  });

  SortingRequestParamsSchema = yup.object<SortingRequestParams>().shape({
    page: yup.number(),
    size: yup.number(),
    direction: yup.string().oneOf(["asc", "desc"]),
    key: yup
      .string()
      .oneOf([
        "id",
        "username",
        "firstName",
        "lastName",
        "email",
        "roles",
        "active",
      ]),
    phrase: yup.string().max(64),
  });
}

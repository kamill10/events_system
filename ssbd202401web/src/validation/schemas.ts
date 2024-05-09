import * as yup from "yup";
import { AccountSingInType } from "../types/Account";
import { PersonalDataType } from "../types/PersonalData";
import { ForgotPasswordType } from "../types/ForgotPassword";

export const signInValidationSchema = yup.object<AccountSingInType>().shape({
    username: yup.string().min(3).max(32).required(),
    password: yup.string().min(8).max(72).required(),
    email: yup.string().email().required(),
    gender: yup.number().required(),
    firstName: yup.string().min(2).max(32).required(),
    lastName: yup.string().min(2).max(64).required()
});

export const changePersonalDataSchema = yup.object<PersonalDataType>().shape({
    gender: yup.number().required(),
    firstName: yup.string().min(2).max(32).required(),
    lastName: yup.string().min(2).max(64).required()
});

export const ForgotPasswordSchema = yup.object<ForgotPasswordType>().shape({
    email: yup.string().email().required()
});
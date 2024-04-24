import * as yup from "yup";
import { AccountSingInType } from "../types/Account";

export const signInValidationSchema = yup.object<AccountSingInType>().shape({
    username: yup.string().min(3).max(32).required(),
    password: yup.string().min(8).max(72).required(),
    email: yup.string().email().required(),
    gender: yup.number().required(),
    firstName: yup.string().min(2).max(32).required(),
    lastName: yup.string().min(2).max(64).required()
});
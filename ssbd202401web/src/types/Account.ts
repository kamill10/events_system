import { AccountTypeEnum } from "./enums/AccountType.enum.ts";
import { LanguageType } from "./enums/LanguageType.enum.ts";

export interface GetAccountType {
  id: string;
  username: string;
  email: string;
  roles: AccountTypeEnum[];
  active: boolean;
  verified: boolean;
  nonLocked: boolean;
}

export interface GetPersonalAccountType extends GetAccountType {
  firstName: string;
  lastName: string;
  language: LanguageType;
  gender: number;
}

export interface GetDetailedAccountType extends GetPersonalAccountType {
  lastSuccessfulLogin: string;
  lastFailedLogin: string;
  lockedUntil: string;
}

export interface UpdatePersonalDataType {
  firstName: string;
  lastName: string;
  gender: number;
}

export interface ChangeMyPasswordType {
  oldPassword: string;
  newPassword: string;
  confirmNewPassword: string;
}

export interface ChangeMyEmailType {
  password: string;
  email: string;
}

export interface ManageAccountType {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  gender: number;
}

export interface ChangeEmailType {
  email: string;
}
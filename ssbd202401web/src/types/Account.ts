import { AccountTypeEnum } from "./AccountType.ts";

export interface AccountType {
  id: string;
  username: string;
  email: string;
  roles: Array<AccountTypeEnum>;
  gender: number;
  firstName: string;
  lastName: string;
}

export interface AccountLoginType {
  username: string;
  password: string;
}

export interface AccountSingInType {
  username: string;
  password: string;
  email: string;
  gender: number;
  firstName: string;
  lastName: string;
  language: string;
}

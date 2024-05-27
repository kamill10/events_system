import { AccountTypeEnum } from "./enums/AccountType.enum.ts";
import { LanguageType } from "./enums/LanguageType.enum.ts";
import {ActionTypeEnum} from "./enums/ActionType.enum.ts";

export interface AccountChangesType {
  id: string;
  username: string;
  email: string;
  roles: AccountTypeEnum[];
  active: boolean;
  verified: boolean;
  nonLocked: boolean;
  failedLoginAttempts: number;
  lastFailedLoginIp: string;
  lastSuccessfulLoginIp: string;
  lastSuccessfulLogin: string;
  lastFailedLogin: string;
  lockedUntil: string;
  gender: number;
  firstName: string;
  lastName: string;
  language: LanguageType;
  accountTheme: string;
  accountTimeZone: string;
  createdAt: string;
  updatedAt: string;
  createdBy: string;
  updatedBy: string;
  actionType: ActionTypeEnum;
}

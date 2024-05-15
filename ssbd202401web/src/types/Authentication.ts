export interface LoginCredentialsType {
  username: string;
  password: string;
}

export interface SignInCredentialsType {
  username: string;
  password: string;
  confirmPassword: string;
  email: string;
  gender: number;
  firstName: string;
  lastName: string;
  language: string;
}

export interface ResetPasswordType {
  token?: string | null;
  newPassword: string;
  confirmNewPassword: string;
}

export type ForgotPasswordType = {
  email: string;
};

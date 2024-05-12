export interface ResetPasswordType {
    token?: string | null
    newPassword: string
    confirmNewPassword: string
}
export interface NotificationType {
  type: "default" | "error" | "success" | "warning" | "info" | undefined;
  description: string;
}
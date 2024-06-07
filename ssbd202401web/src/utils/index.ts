export function isInstanceOf<T>(value: any, fieldName: string): value is T {
  return fieldName in value;
}

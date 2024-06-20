import { Dayjs } from "dayjs";
import { HTMLInputTypeAttribute } from "react";
import {
  Control,
  FieldErrors,
  FieldValues,
  Path,
  UseFormTrigger,
} from "react-hook-form";

export interface TextFieldProps<T extends FieldValues> {
  control: Control<T>;
  type: HTMLInputTypeAttribute;
  disabled?: boolean;
  errors: FieldErrors<T>;
  trigger: UseFormTrigger<T>;
  name: Path<T>;
  label: string;
  focused?: boolean;
  multiline?: boolean;
  rows?: number;
}

export interface DatePickerProps<T extends FieldValues>
  extends TextFieldProps<T> {
  whatToValidate?: string[];
  minDate?: Dayjs;
  maxDate?: Dayjs;
}

export interface RouteType {
  page: React.FunctionComponent;
  pathname: string;
  name: string;
  renderOnNavbar: boolean;
  renderOnDropdown: boolean;
}

export interface PublicLayoutPropType {
  page: React.FunctionComponent;
}

export default interface NavbarPropType {
  routes: RouteType[];
}

export interface LinkPropType {
  href: string;
  name: string;
  onClick: () => void;
  handleClose: () => void;
  route?: RouteType;
}

export interface GenderListProps<T extends FieldValues> {
  control: Control<T>;
  disabled?: boolean;
  errors: FieldErrors<T>;
  name: Path<T>;
}

export interface MenuProps {
  routes: RouteType[];
  anchorElUser: HTMLElement | null;
  setAnchorElUser: (element: null | HTMLElement) => void;
}

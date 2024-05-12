export interface RouteType {
  page: React.FunctionComponent;
  pathname: string;
  name: string;
  renderOnNavbar: boolean;
  renderOnDropdown: boolean;
}

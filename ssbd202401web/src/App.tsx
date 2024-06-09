import { BrowserRouter } from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";
import { SnackbarProvider } from "notistack";
import LoadingScreen from "./pages/LoadingScreen.tsx";
import { ManageAccountsStateContextProvider } from "./context/ManageAccountsContext.tsx";
import RefreshToken from "./pages/RefreshToken.tsx";
import { EventsStateContextProvider } from "./context/EventsContext.tsx";
import ScrollToTop from "./router/ScrollToTop.tsx";
import { LocationsStateContextProvider } from "./context/LocationsContext.tsx";
import { LocalizationProvider } from "@mui/x-date-pickers";
import { AdapterDayjs } from "@mui/x-date-pickers/AdapterDayjs";

function App() {
  return (
    <SnackbarProvider>
      <AccountStateContextProvider>
        <ManageAccountsStateContextProvider>
          <EventsStateContextProvider>
            <LocationsStateContextProvider>
              <LocalizationProvider dateAdapter={AdapterDayjs}>
                <BrowserRouter>
                  <ScrollToTop></ScrollToTop>
                  <RouterComponent></RouterComponent>
                  <LoadingScreen></LoadingScreen>
                  <RefreshToken></RefreshToken>
                </BrowserRouter>
              </LocalizationProvider>
            </LocationsStateContextProvider>
          </EventsStateContextProvider>
        </ManageAccountsStateContextProvider>
      </AccountStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

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
import { SpeakersStateContextProvider } from "./context/SpeakersContext.tsx";
import { LoadingScreenStateContextProvider } from "./context/LoadingScreenContext.tsx";

function App() {
  return (
    <SnackbarProvider>
      <LoadingScreenStateContextProvider>
        <AccountStateContextProvider>
          <ManageAccountsStateContextProvider>
            <EventsStateContextProvider>
              <LocationsStateContextProvider>
                <SpeakersStateContextProvider>
                  <LocalizationProvider dateAdapter={AdapterDayjs}>
                    <BrowserRouter>
                      <ScrollToTop></ScrollToTop>
                      <RouterComponent></RouterComponent>
                      <LoadingScreen></LoadingScreen>
                      <RefreshToken></RefreshToken>
                    </BrowserRouter>
                  </LocalizationProvider>
                </SpeakersStateContextProvider>
              </LocationsStateContextProvider>
            </EventsStateContextProvider>
          </ManageAccountsStateContextProvider>
        </AccountStateContextProvider>
      </LoadingScreenStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

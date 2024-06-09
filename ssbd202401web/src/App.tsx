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
import { SpeakersStateContextProvider } from "./context/SpeakersContext.tsx";

function App() {
  return (
    <SnackbarProvider>
      <AccountStateContextProvider>
        <ManageAccountsStateContextProvider>
          <EventsStateContextProvider>
            <LocationsStateContextProvider>
              <SpeakersStateContextProvider>
                <BrowserRouter>
                  <ScrollToTop></ScrollToTop>
                  <RouterComponent></RouterComponent>
                  <LoadingScreen></LoadingScreen>
                  <RefreshToken></RefreshToken>
                </BrowserRouter>
              </SpeakersStateContextProvider>
            </LocationsStateContextProvider>
          </EventsStateContextProvider>
        </ManageAccountsStateContextProvider>
      </AccountStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

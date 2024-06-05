import { BrowserRouter } from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";
import { SnackbarProvider } from "notistack";
import LoadingScreen from "./pages/LoadingScreen.tsx";
import { ManageAccountsStateContextProvider } from "./context/ManageAccountsContext.tsx";
import RefreshToken from "./pages/RefreshToken.tsx";
import { EventsStateContextProvider } from "./context/EventsContext.tsx";

function App() {
  return (
    <SnackbarProvider>
      <AccountStateContextProvider>
        <ManageAccountsStateContextProvider>
          <EventsStateContextProvider>
            <BrowserRouter>
              <RouterComponent></RouterComponent>
              <LoadingScreen></LoadingScreen>
              <RefreshToken></RefreshToken>
            </BrowserRouter>
          </EventsStateContextProvider>
        </ManageAccountsStateContextProvider>
      </AccountStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

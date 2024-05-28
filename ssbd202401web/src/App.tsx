import { BrowserRouter } from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";
import { SnackbarProvider } from "notistack";
import LoadingScreen from "./pages/LoadingScreen.tsx";
import { ManageAccountsStateContextProvider } from "./context/ManageAccountsContext.tsx";
import RefreshToken from "./pages/RefreshToken.tsx";

function App() {
  return (
    <SnackbarProvider>
      <AccountStateContextProvider>
        <ManageAccountsStateContextProvider>
          <BrowserRouter>
            <RouterComponent></RouterComponent>
            <LoadingScreen></LoadingScreen>
            <RefreshToken></RefreshToken>
          </BrowserRouter>
        </ManageAccountsStateContextProvider>
      </AccountStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

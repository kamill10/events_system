import { BrowserRouter } from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";
import { SnackbarProvider } from "notistack";
import LoadingScreen from "./pages/LoadingScreen.tsx";

function App() {
  return (
    <SnackbarProvider>
      <AccountStateContextProvider>
        <BrowserRouter>
          <RouterComponent></RouterComponent>
          <LoadingScreen></LoadingScreen>
        </BrowserRouter>
      </AccountStateContextProvider>
    </SnackbarProvider>
  );
}

export default App;

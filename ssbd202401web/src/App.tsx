import {BrowserRouter} from "react-router-dom";
import RouterComponent from "./router/RouterComponent.tsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { AccountStateContextProvider } from "./context/AccountContext.tsx";
import { SnackbarProvider } from "notistack";

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AccountStateContextProvider>
        <SnackbarProvider>
        <BrowserRouter>
          <RouterComponent></RouterComponent>
        </BrowserRouter>
        </SnackbarProvider>
      </AccountStateContextProvider>
    </QueryClientProvider>
  )
}

export default App

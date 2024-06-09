import { LocationsWithNumberOfElements } from "../types/Location.ts";
import { createContext, ReactNode, useContext, useState } from "react";

interface ManageLocations {
  locations: LocationsWithNumberOfElements | null;
  setLocations: (locations: LocationsWithNumberOfElements) => void;
}

const LocationsStateContext = createContext<ManageLocations | null>(null);

export const LocationsStateContextProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [locations, setLocations] =
    useState<LocationsWithNumberOfElements | null>(null);

  return (
    <LocationsStateContext.Provider
      value={{
        locations,
        setLocations,
      }}
    >
      {children}
    </LocationsStateContext.Provider>
  );
};

export const useLocationsState = () => {
  const locationsState = useContext(LocationsStateContext);

  if (!locationsState) {
    throw new Error("You forgot about LocationsStateContext");
  }

  return locationsState;
};

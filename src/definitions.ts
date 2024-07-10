export interface PaysafePluginPlugin {
  echo(): Promise<void>;
  startVenmo(): Promise<string>;
}

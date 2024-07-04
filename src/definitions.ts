export interface PaysafePluginPlugin {
  echo(options: { value: string }): Promise<{ value: string }>;
}

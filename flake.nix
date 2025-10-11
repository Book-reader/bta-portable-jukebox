{
	inputs = {
		nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
	};
	outputs = inputs:
	let
		pkgs = import inputs.nixpkgs {};
	in {
		devShells.${builtins.currentSystem}.default = pkgs.mkShellNoCC {
			packages = with pkgs; [ libglvnd jdk21 openal alsa-lib jetbrains.idea-community ];
			# For if I need to use raylib
			LD_LIBRARY_PATH = with pkgs; ''$LD_LIBRARY_PATH:${
				lib.makeLibraryPath [
					libGL
					libglvnd
					openal
					alsa-lib
					jetbrains.idea-community
				]
			}'';

		};
	};
}

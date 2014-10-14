package models;

public enum TileType{
	Grass{
		@Override
		public String toString(){
			return "0 - Grass";
		}
	},
	CastleTopLeft{
		@Override
		public String toString(){
			return "1 - CastleTopLeft";
		}
	},
	CastleTopRight{
		@Override
		public String toString(){
			return "2 - CastleTopRight";
		}
	},
	CastleBottomLeft{
		@Override
		public String toString(){
			return "3 - CastleBottomLeft";
		}
	},
	CastleBottomRight{
		@Override
		public String toString(){
			return "4 - CastleBottomRight";
		}
	},
	Rock{
		@Override
		public String toString(){
			return "5 - Rock";
		}
	}
		
}

package dev.craigmc08.techmod.blocks;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import team.reborn.energy.Energy;

public class WireBlock extends Block {
  public static final BooleanProperty NORTH = BooleanProperty.of("north");
  public static final BooleanProperty EAST = BooleanProperty.of("east");
  public static final BooleanProperty SOUTH = BooleanProperty.of("south");
  public static final BooleanProperty WEST = BooleanProperty.of("west");
  public static final BooleanProperty UP = BooleanProperty.of("up");
  public static final BooleanProperty DOWN = BooleanProperty.of("down");

  public static final VoxelShape unconnectedShape = Block.createCuboidShape(6, 6, 6, 10, 10, 10);
  public static final Map<Direction, VoxelShape> connectedShapes;
  public static final Map<BlockState, VoxelShape> shapes = Maps.newHashMap();

  public WireBlock(Settings settings) {
    super(settings);
    setDefaultState(getStateManager().getDefaultState()
      .with(NORTH, false)
      .with(EAST, false)
      .with(SOUTH, false)
      .with(WEST, false)
      .with(UP, false)
      .with(DOWN, false)
    );

    // Create voxel shape for every possible state
    for (BlockState state : getStateManager().getStates()) {
      VoxelShape shape = unconnectedShape;
      for (Direction dir : Direction.values()) {
        if (state.get(getPropertyForDirection(dir))) {
          shape = VoxelShapes.union(shape, connectedShapes.get(dir));
        }
      }
      shapes.put(state, shape);
    }
  }

  @Override
  protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
    stateManager.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
  }

  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Map<Direction, Boolean> connected = new HashMap<>();
    for (Direction direction : Direction.values()) {
      BlockPos nPos = ctx.getBlockPos().offset(direction);
      BlockEntity neighbor = ctx.getWorld().getBlockEntity(nPos);
      connected.put(direction, Energy.valid(neighbor));
    }
    System.out.println("determing placement state");
    return getDefaultState()
      .with(NORTH, connected.get(Direction.NORTH))
      .with(EAST, connected.get(Direction.EAST))
      .with(SOUTH, connected.get(Direction.SOUTH))
      .with(WEST, connected.get(Direction.WEST))
      .with(UP, connected.get(Direction.UP))
      .with(DOWN, connected.get(Direction.DOWN));
  }

  public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
    BooleanProperty prop = getPropertyForDirection(direction);
    System.out.println("updated at " + pos.toString());
    return state.with(prop, isConnected(direction, pos, world, newState));
  }


  @Override
  public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
    return shapes.get(state);
  }

  public boolean isConnected(Direction direction, BlockPos pos, WorldAccess world, BlockState newState) {
    BlockPos nPos = pos.offset(direction);
    BlockEntity neighbor = world.getBlockEntity(nPos);
    return Energy.valid(neighbor) || newState.getBlock().is(this);
  }

  public BooleanProperty getPropertyForDirection(Direction direction) {
    switch (direction) {
      case NORTH: return NORTH;
      case EAST: return EAST;
      case SOUTH: return SOUTH;
      case WEST: return WEST;
      case UP: return UP;
      case DOWN: return DOWN;

      // Unreachable
      default: return null;
    }
  }

  static {
    // Basic connection shapes
    connectedShapes = Maps.newEnumMap(ImmutableMap.<Direction, VoxelShape>builder()
      .put(Direction.NORTH, Block.createCuboidShape(6, 6, 0, 10, 10, 10))
      .put(Direction.SOUTH, Block.createCuboidShape(6, 6, 6, 10, 10, 16))
      .put(Direction.EAST, Block.createCuboidShape(6, 6, 6, 16, 10, 10))
      .put(Direction.WEST, Block.createCuboidShape(0, 6, 6, 10, 10, 10))
      .put(Direction.UP, Block.createCuboidShape(6, 6, 6, 10, 16, 10))
      .put(Direction.DOWN, Block.createCuboidShape(6, 0, 6, 10, 10, 10))
      .build()
    );
  }
}
